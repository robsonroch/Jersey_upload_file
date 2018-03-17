package br.com.devmedia.webservice.model.dao;

import br.com.devmedia.webservice.exceptions.DAOException;
import br.com.devmedia.webservice.exceptions.ErrorCode;
import br.com.devmedia.webservice.model.domain.Projeto;

import javax.persistence.EntityManager;
import java.util.List;

public class ProjetoDAO {

    public List<Projeto> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Projeto> projetos;

        try {
            projetos = em.createQuery("select m from Projeto m", Projeto.class).getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao recuperar todas as projetos do banco: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return projetos;
    }

    public Projeto getById(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Projeto projeto;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            projeto = em.find(Projeto.class, id);
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar projeto por id no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (projeto == null) {
            throw new DAOException("Projeto de id " + id + " não existe.", ErrorCode.NOT_FOUND.getCode());
        }

        return projeto;
    }

    public Projeto save(Projeto projeto) {
        EntityManager em = JPAUtil.getEntityManager();

        if (!projetoIsValid(projeto)) {
            throw new DAOException("Projeto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            em.persist(projeto);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao salvar projeto no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        return projeto;
    }

    public Projeto update(Projeto projeto) {
        EntityManager em = JPAUtil.getEntityManager();
        Projeto projetoManaged;

        if (projeto.getId() <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (!projetoIsValid(projeto)) {
            throw new DAOException("Projeto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            projetoManaged = em.find(Projeto.class, projeto.getId());
            projetoManaged.setNome(projeto.getNome());
            projetoManaged.setDescricao(projeto.getDescricao());
            em.getTransaction().commit();
        } catch (NullPointerException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Projeto informada para atualização não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao atualizar projeto no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        return projetoManaged;
    }

    public Projeto delete(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Projeto projeto;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            projeto = em.find(Projeto.class, id);
            em.remove(projeto);
            em.getTransaction().commit();
        } catch (IllegalArgumentException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Projeto informada para remoção não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao remover projeto do banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return projeto;
    }

    public List<Projeto> getByPagination(int firstResult, int maxResults) {
        List<Projeto> projetos;
        EntityManager em = JPAUtil.getEntityManager();

        try {
            projetos = em.createQuery("select m from Projeto m", Projeto.class)
                    .setFirstResult(firstResult - 1)
                    .setMaxResults(maxResults)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar projetos no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (projetos.isEmpty()) {
            throw new DAOException("Página com projetos vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return projetos;
    }

    public List<Projeto> getByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Projeto> projetos;

        try {
            projetos = em.createQuery("select m from Projeto m where m.nome like :name", Projeto.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar projetos por nome no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (projetos.isEmpty()) {
            throw new DAOException("A consulta não encontrou projetos.", ErrorCode.NOT_FOUND.getCode());
        }

        return projetos;
    }

    private boolean projetoIsValid(Projeto projeto) {
        try {
            if ((projeto.getNome().isEmpty()) || (projeto.getDescricao().isEmpty()))
                return false;
        } catch (NullPointerException ex) {
            throw new DAOException("Projeto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;
    }
}
