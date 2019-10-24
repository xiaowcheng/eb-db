package com.ebupt.txcy.yellowpagelibbak.repository;

import java.util.List;

import com.ebupt.txcy.serviceapi.Entity.Yellowpagelibbak;
import com.ebupt.txcy.serviceapi.Entity.YellowpagelibbakId;
import com.ebupt.txcy.serviceapi.Entity.Whitelist;
import org.springframework.data.jpa.repository. JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface YellowpagelibbakRepository extends JpaRepository<Yellowpagelibbak, YellowpagelibbakId>, JpaSpecificationExecutor<Yellowpagelibbak>{
	 List<Yellowpagelibbak> findByPhoneNumberAndCreateTime(String phoneNumber, String createTime);
	 List<Yellowpagelibbak> findByCreateTime(String createTime);
	 List<Yellowpagelibbak> findByPhoneNumber(String phoneNumber);
	Yellowpagelibbak getByPhoneNumber(String phoneNumber);
	void deleteByPhoneNumber(String phoneNumber);
}
