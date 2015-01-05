package usine;

import java.io.Serializable;

public class MessageApprovisonnement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7487415122423011124L;
	private int quantite;
	private String bois;
	private MessageCommande commande;
	
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public String getBois() {
		return bois;
	}
	public void setBois(String bois) {
		this.bois = bois;
	}
	public MessageCommande getCommande() {
		return commande;
	}
	public void setCommande(MessageCommande commande) {
		this.commande = commande;
	}
	
	
}
