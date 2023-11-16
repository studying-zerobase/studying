package com.zerobase.munbanggu.study.repository;

import com.zerobase.munbanggu.study.model.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist,Long> {

}
