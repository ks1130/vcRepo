package com.example.vocabularycard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column(name="login_id")
	private String loginId;

	//@Column(name="name")
	//private String name;

	@Column(name="password")
	private String password;

	@Column(name="role")
	private String role;

	@Column(name="enabled")
	private Boolean enabled;
}
