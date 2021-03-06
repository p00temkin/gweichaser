package crypto.respawned.gweichaser;

import crypto.forestfish.enums.EVMChain;
import crypto.forestfish.utils.SystemUtils;

public class GweiSettings {

	private EVMChain chain = EVMChain.ETHEREUM;
    private Integer gweiThreshold = 20;
    private Integer repeatbelowThreshold = 2;
    private Integer pollIntervalinSeconds = 60;
    private String providerURL = "";
    private String apiTokenApp = "xxxxxxxxx";
    private String apiTokenUser = "xxxxxxxxx";
    
    public GweiSettings() {
        super();
    }

    public Integer getGweiThreshold() {
        return gweiThreshold;
    }

    public void setGweiThreshold(Integer gweiThreshold) {
        this.gweiThreshold = gweiThreshold;
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

	public void print() {
        System.out.println("Settings:");
        System.out.println(" - chain: " + this.getChain());
        System.out.println(" - gweiThreshold: " + this.getGweiThreshold());
        System.out.println(" - pollIntervalinSeconds: " + this.getPollIntervalinSeconds());
        System.out.println(" - repeatbelowThreshold: " + this.getRepeatbelowThreshold());
        System.out.println(" - providerURL: " + this.getProviderURL());
        System.out.println(" - apiTokenApp: " + this.getApiTokenApp());
        System.out.println(" - apiTokenUser: " + this.getApiTokenUser());
    }

    public void sanityCheck() {
        boolean allgood = true;
        if (this.getGweiThreshold()<=0) {
            System.out.println(" - gweiThreshold INVALID: " + this.getGweiThreshold());
            allgood = false;
        }
        
        if (!allgood) {
            SystemUtils.halt();
        }
    }
    
}
