package com.company.hrsystem.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.company.hrsystem.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class RequestTypeModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer requestTypeId;

	private String requestTypeName;

	private Boolean deletedFlag;

	@JsonFormat(timezone=DateUtil.TIME_ZONE)
	private Timestamp updatedAt;

}