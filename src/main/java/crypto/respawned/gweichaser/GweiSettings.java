package crypto.respawned.gweichaser;

import crypto.forestfish.enums.evm.EVMChain;
import crypto.forestfish.utils.SystemUtils;

public class GweiSettings {

	private EVMChain chain = EVMChain.ETHEREUM;
    private Double gweiThreshold = 20.0d;
    private Integer repeatbelowThreshold = 3;
    private Integer pollIntervalinSeconds = 60;
    private String providerURL = "";
    private String apiTokenApp = "xxxxxxxxx";
    private String apiTokenUser = "xxxxxxxxx";
    private boolean nodeOptimize = false;
    private boolean continousMode = false;
    
    public GweiSettings() {
        super();
    }

    public String getProviderURL() {
        return providerURL;
    }

    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    public String getApiTokenApp() {
        return apiTokenApp;
    }
    
    public void setApiTokenApp(String apiTokenApp) {
        this.apiTokenApp = apiTokenApp;
    }

    public String getApiTokenUser() {
        return apiTokenUser;
    }
    
    public void setApiTokenUser(String apiTokenUser) {
        this.apiTokenUser = apiTokenUser;
    }

    public Integer getPollIntervalinSeconds() {
        return pollIntervalinSeconds;
    }

    public void setPollIntervalinSeconds(Integer pollIntervalinSeconds) {
        this.pollIntervalinSeconds = pollIntervalinSeconds;
    }

    public Integer getRepeatbelowThreshold() {
        return repeatbelowThreshold;
    }

    public void setRepeatbelowThreshold(Integer repeatbelowThreshold) {
        this.repeatbelowThreshold = repeatbelowThreshold;
    }

    public EVMChain getChain() {
		return chain;
	}

	public void setChain(EVMChain chain) {
		this.chain = chain;
	}

	public boolean isContinousMode() {
		return continousMode;
	}

	public void setContinousMode(boolean continousMode) {
		this.continousMode = continousMode;
	}

	public void print() {
        System.out.println("Settings:");
        System.out.println(" - chain: " + this.getChain());
        System.out.println(" - gweiThreshold: " + this.getGweiThreshold());
        System.out.println(" - pollIntervalinSeconds: " + this.getPollIntervalinSeconds());
        System.out.println(" - repeatbelowThreshold: " + this.getRepeatbelowThreshold());
        System.out.println(" - continousMode: " + this.isContinousMode());
        if (!"".equals(this.getProviderURL())) System.out.println(" - providerURL: " + this.getProviderURL());
        if (!"xxxxxxxxx".equals(this.getApiTokenApp())) System.out.println(" - apiTokenApp: " + this.getApiTokenApp());
        if (!"xxxxxxxxx".equals(this.getApiTokenUser())) System.out.println(" - apiTokenUser: " + this.getApiTokenUser());
        System.out.println(" - nodeOptimize: " + this.isNodeOptimize());
    }

    public void sanityCheck() {
        boolean allgood = true;
        if (this.getGweiThreshold()<=0.0d) {
            System.out.println(" - gweiThreshold INVALID: " + this.getGweiThreshold());
            allgood = false;
        }
        
        if (!allgood) {
            SystemUtils.halt();
        }
    }

	public boolean isNodeOptimize() {
		return nodeOptimize;
	}

	public void setNodeOptimize(boolean nodeOptimize) {
		this.nodeOptimize = nodeOptimize;
	}

	public Double getGweiThreshold() {
		return gweiThreshold;
	}

	public void setGweiThreshold(Double gweiThreshold) {
		this.gweiThreshold = gweiThreshold;
	}
    
}
