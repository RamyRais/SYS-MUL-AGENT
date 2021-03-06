package usine;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Atelier1Agent extends Agent{
	
	private static final int MAX_STOCK_TABLE = 20;
	private static final int MAX_STOCK_CHAISE = 120; 
	private static final int MAX_STOCK_BUFFET = 20;
	private static final int PLANCHE_LIT = 5;
	private static final int PLANCHE_CHAISE = 2;
	private static final int PLANCHE_BUFFET = 6;
	private int table;
	private int chaise;
	private int buffet;
	
	
	protected void setup() {
		this.table = MAX_STOCK_TABLE;
		this.chaise = MAX_STOCK_CHAISE;
		this.buffet = MAX_STOCK_BUFFET;
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// réception des messages
				recuCommande();
			}
		});
		
		
	}
	
	public void recuCommande(){
		ACLMessage msg = receive();
		if (msg != null) {
			if(msg.getSender().getLocalName().equals("commercial")){
				MessageCommande commandes = null;
				try {
					commandes = (MessageCommande)	msg.getContentObject();
					//doWait(X); X temps pour traiter le commande
					this.traitementCommande(commandes);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.getSender().getLocalName().equals("approvisonnement")){
				//TODO reception du msg de l'approvisonnement
				MessageApprovisonnement approv = null;
				try {
					approv = (MessageApprovisonnement) msg.getContentObject();
					MessageCommande com = approv.getCommande();
					//doWait(X); X temps pour fabriquer l'elt
					String s = approv.getCommande().getCommandes().get(0);
					String[] s1 = s.split(" ");
					if(s1[0].contains("table")){
						this.table = this.MAX_STOCK_TABLE;
					}
					if(s1[0].contains("chaise")){
						this.chaise = this.MAX_STOCK_CHAISE;
					}
					if(s1[0].contains("buffet")){
						this.buffet = this.MAX_STOCK_BUFFET;
					}
					
					this.envoiCommandeCommercial(com);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void traitementCommande(MessageCommande commandes){
		ArrayList<String> c = commandes.getCommandes();
		for (String commande : c) {
			if( commande.contains("table")){
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeTable(com);
			}
			if( commande.contains("chaise")){
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeChaise(com);
			}
			if( commande.contains("buffet")){
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeBuffet(com);
			}
		}	
	}
	
	private void traitementCommandeTable(MessageCommande commandes){
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
			if(Integer.parseInt(s[1]) < this.table ){
				// TODO envoi commande au commercial avec déc du nombre des tables du stock
				this.table -= Integer.parseInt(s[1]);
				this.envoiCommandeCommercial(commandes);			
			} else {
				// TODO envoi msg lil approvi bech na5o sel3a wne5dem
				MessageApprovisonnement appro = new MessageApprovisonnement();
				appro.setCommande(commandes);
				appro.setBois("chene");
				appro.setQuantite(Integer.parseInt(s[1]+this.MAX_STOCK_TABLE)*Atelier1Agent.PLANCHE_LIT);
				this.approChene(appro);
			}
	}
	
	private void traitementCommandeChaise(MessageCommande commandes){
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
			if(Integer.parseInt(s[1]) < this.chaise ){
				// TODO envoi commande au commercial avec déc du nombre des chaises du stock
				this.chaise -= Integer.parseInt(s[1]);
				this.envoiCommandeCommercial(commandes);			
			} else {
				// TODO envoi msg lil approvi bech na5o sel3a wne5dem
				MessageApprovisonnement appro = new MessageApprovisonnement();
				appro.setCommande(commandes);
				appro.setBois("chene");
				appro.setQuantite(Integer.parseInt(s[1]+this.MAX_STOCK_CHAISE)*Atelier1Agent.PLANCHE_CHAISE);
				this.approChene(appro);
			}
	}
	
	private void traitementCommandeBuffet(MessageCommande commandes){
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
			if(Integer.parseInt(s[1]) <= this.buffet ){
				// TODO envoi commande au commercial avec déc du nombre des buffets du stock
				this.buffet -= Integer.parseInt(s[1]);
				this.envoiCommandeCommercial(commandes);			
			} else {
				// TODO envoi msg lil approvi bech na5o sel3a wne5dem
				MessageApprovisonnement appro = new MessageApprovisonnement();
				appro.setCommande(commandes);
				appro.setBois("chene");
				appro.setQuantite(Integer.parseInt(s[1]+this.MAX_STOCK_BUFFET)*Atelier1Agent.PLANCHE_BUFFET);
				this.approChene(appro);
				
			}
	}
	
	public void envoiCommandeCommercial(MessageCommande commandes) {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.addReceiver(new AID("commercial", AID.ISLOCALNAME));
		try {
			msg.setContentObject(commandes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(msg);
	}
	
	
	public void approChene(MessageApprovisonnement commande){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("approvisonnement", AID.ISLOCALNAME));
		try {
			msg.setContentObject(commande);
			send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
