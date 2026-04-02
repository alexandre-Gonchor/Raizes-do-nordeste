package com.raizesdonordeste.backend;

import com.raizesdonordeste.backend.dominio.Produto;
import com.raizesdonordeste.backend.infra.Produto_repositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

//	@Bean
//	CommandLineRunner testarBanco(Produto_repositorio repo) {
//		return args -> {
//			Produto p1 = new Produto();
//			p1.setNome("Cuscuz com Ovo");
//			p1.setPreco(new BigDecimal("12.50"));
//			p1.setDisponivel(true);
//			p1.setDescricao("Cuscuz quentinho com dois ovos fritos na manteiga.");
//
//			repo.save(p1);
//
//			System.out.println("--------------------------------------");
//			System.out.println("SUCESSO: O Cuscuz foi salvo no banco!");
//			System.out.println("--------------------------------------");
//		};
//	}
}