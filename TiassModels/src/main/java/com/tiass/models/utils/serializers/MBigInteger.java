package com.tiass.models.utils.serializers;

import java.math.BigDecimal; 

public class MBigInteger  extends BigDecimal  { 
	private static final long serialVersionUID = -2716353346644300558L; 
	public MBigInteger(String val) {
		super(val); 
	}
	public MBigInteger( ) { 
		super(BigDecimal.ZERO.intValue());
	}
}
