package com.ebupt.txcy.yellowpagelibbak.repository;


import com.ebupt.txcy.yellowpagelibbak.entity.Yellowpagelibbak;
import com.ebupt.txcy.yellowpagelibbak.entity.YellowpagelibbakId;
import org.springframework.data.jpa.repository. JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YellowpagelibbakRepository extends JpaRepository<Yellowpagelibbak, YellowpagelibbakId>, JpaSpecificationExecutor<Yellowpagelibbak>{
	 List<Yellowpagelibbak> findByPhoneNumberAndCreateTime(String phoneNumber, String createTime);
	 List<Yellowpagelibbak> findByCreateTime(String createTime);
	 List<Yellowpagelibbak> findByPhoneNumber(String phoneNumber);
	void deleteByPhoneNumber(String phoneNumber);
}
