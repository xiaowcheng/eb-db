package com.ebupt.txcy.yellowpagelibbak.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TXCY_yellowpagelibbak")
@ApiModel
@IdClass(YellowpagelibbakId.class)
public class Yellowpagelibbak {
   @Id
   @Column(name = "phonenumber",nullable = false,length = 16)
   @ApiModelProperty(name = "phoneNumber",notes = "电话号码")
   private String phoneNumber;
   @ApiModelProperty(name = "classAType",notes = "号码类型")
   @Column(name = "classatype",length = 1)
   private String  classAType;
   @ApiModelProperty(name = "profession",notes = "行业归属")
   @Column(name="profession",length = 100)
   private String  profession;
   @ApiModelProperty(name = "classBType",notes = "号码详细描述号码详细描述")
   @Column(name="classbtype")
   private String  classBType;

   @Id
   @Column(name = "sourceid",nullable = false)
   @ApiModelProperty(name = "sourceId",notes = "数据来源")
   private Integer  sourceId;
   @CreationTimestamp
   @Column(name="createtime")
   @ApiModelProperty(name = "createTime",notes = "创建时间")
   @Temporal(TemporalType.TIMESTAMP)
   private Date createTime;
}
