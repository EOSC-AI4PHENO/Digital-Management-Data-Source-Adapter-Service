package com.siseth.adapter.repository.source;

import com.siseth.adapter.entity.source.SourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceConfigRepository extends JpaRepository<SourceConfig, Long> {
}
