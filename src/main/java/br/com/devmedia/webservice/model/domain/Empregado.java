package br.com.devmedia.webservice.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Empregado {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cargo;
        
    @ManyToMany(mappedBy = "empregados", cascade={CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Projeto> projetos;

}
