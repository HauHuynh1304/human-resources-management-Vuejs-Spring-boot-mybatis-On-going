package com.company.hrsystem.dto;

import com.company.hrsystem.model.PersonalInfoModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PersonalInfoDto extends PersonalInfoModel {

	private static final long serialVersionUID = 1L;

}