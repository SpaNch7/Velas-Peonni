package com.peonni.velas.repository;

import com.peonni.velas.entity.LogProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogProdutoRepository extends JpaRepository<LogProduto, Long> {
    List<LogProduto> findByProdutoId(Long produtoId);
}
