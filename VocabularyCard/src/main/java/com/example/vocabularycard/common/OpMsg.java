package com.example.vocabularycard.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpMsg {
	private String msgType;
	private String msgText;
}
