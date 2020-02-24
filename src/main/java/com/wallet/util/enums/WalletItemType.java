package com.wallet.util.enums;

public enum WalletItemType {
	IN("INBOUND"),
	OU("OUTBOUND");
	
	private final String value;
	
	WalletItemType(String description) {
		this.value = description;
	}
	
	public String getValue() {
		return this.value;
	}

	public static WalletItemType getEnum(String description) {
		for(WalletItemType t: values()) {
			if(t.getValue().equals(description)) {
				return t;
			}
		}
		return null;
	}
}
