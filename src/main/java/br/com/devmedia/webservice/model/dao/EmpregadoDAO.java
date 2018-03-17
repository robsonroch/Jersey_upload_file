package br.com.devmedia.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.webservice.exceptions.DAOException;
import br.com.devmedia.webservice.exceptions.ErrorCode;
import br.com.devmedia.webservice.model.domain.Empregado;

public class EmpregadoDAO {

    public List<Empregado> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Empregado> empregados;

        try {
            empregados = em.createQuery("select e from Empregado e", Empregado.class)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao recuperar todos os empregados do banco: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return empregados;
    }

    public Empregado getById(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Empregado empregado;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            empregado = em.find(Empregado.class, id);
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar empregado por id no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (empregado == null) {
            throw new DAOException("Empregado de id " + id + " não existe.", ErrorCode.NOT_FOUND.getCode());
        }

        return empregado;
    }

    public Empregado save(Empregado empregado) {
        EntityManager em = JPAUtil.getEntityManager();

        if (!empregadoIsValid(empregado)) {
            throw new DAOException("Empregado com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            em.persist(empregado);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao salvar empregado no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        return empregado;
    }

    public Empregado update(Empregado empregado) {
        EntityManager em = JPAUtil.getEntityManager();
        Empregado empregadoManaged;

        if (empregado.getId() <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (!empregadoIsValid(empregado)) {
            throw new DAOException("Empregado com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            empregadoManaged = em.find(Empregado.class, empregado.getId());
            empregadoManaged.setNome(empregado.getNome());
            empregadoManaged.setCargo(empregado.getCargo());
            em.getTransaction().commit();
        } catch (NullPointerException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Empregado informado para atualização não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao atualizar empregado no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        return empregadoManaged;
    }

    public Empregado delete(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Empregado empregado;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            empregado = em.find(Empregado.class, id);
            em.remove(empregado);
            em.getTransaction().commit();
        } catch (IllegalArgumentException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Empregado informado para remoção não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao remover empregado do banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return empregado;
    }

    public List<Empregado> getByPagination(int firstResult, int maxResults) {
        List<Empregado> empregados;
        EntityManager em = JPAUtil.getEntityManager();

        try {
            empregados = em.createQuery("select p from Empregado p", Empregado.class)
                    .setFirstResult(firstResult - 1)
                    .setMaxResults(maxResults)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar empregados no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (empregados.isEmpty()) {
            throw new DAOException("Página com empregados vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return empregados;
    }

    public List<Empregado> getByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Empregado> empregados;

        try {
            empregados = em.createQuery("select p from Empregado p where p.nome like :name", Empregado.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar empregados por nome no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if (empregados.isEmpty()) {
            throw new DAOException("A consulta não encontrou empregados.", ErrorCode.NOT_FOUND.getCode());
        }

        return empregados;
    }

    private boolean empregadoIsValid(Empregado empregado) {
        try {
            if ((empregado.getNome().isEmpty()) || (empregado.getCargo().isEmpty()))
                return false;
        } catch (NullPointerException ex) {
            throw new DAOException("Empregado com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;
    }
}