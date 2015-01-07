package usine;

import java.util.ArrayList;

import java.io.Serializable;

public class MessageCommande implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7849494915476667691L;
	private String nomClient;
	private ArrayList<String> commandes; // format d'une commande "<type object> <quatité>"
	
	public String getNomClient() {
		return nomClient;
	}
	
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}
	
	public ArrayList<String> getCommandes() {
		return commandes;
	}
	
	public void setCommandes(ArrayList<String> commandes) {
		this.commandes = commandes;
	}
	
//	public String toString(){
//		return "Nom client: "+this.nomClient +":\nCommandes: "+ this.commandes.toString();
//		
//	}
	
}
