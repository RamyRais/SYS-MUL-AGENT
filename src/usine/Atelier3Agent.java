package usine;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

public class Atelier3Agent extends Agent {

	private static final int MAX_STOCK_BANQUETTE = 30;
	private static final int MAX_STOCK_FAUTEUIL = 60;
	private static final int MAX_STOCK_ETAGERE = 30;
	private static final int PLANCHE_BANQUETTE = 6;
	private static final int PLANCHE_FAUTEUIL = 3;
	private static final int PLANCHE_ETAGERE = 8;
	private int banquette;
	private int fauteuil;
	private int etagere;

	protected void setup() {
		this.banquette = MAX_STOCK_BANQUETTE;
		this.fauteuil = MAX_STOCK_FAUTEUIL;
		this.etagere = MAX_STOCK_ETAGERE;
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// réception des messages
				recuCommande();
			}
		});

	}

	public void recuCommande() {
		ACLMessage msg = receive();
		if (msg != null) {
			if (msg.getSender().getLocalName().equals("commercial")) {
				MessageCommande commandes = null;
				try {
					commandes = (MessageCommande) msg.getContentObject();
					// doWait(X); X temps pour traiter le commande
					this.traitementCommande(commandes);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (msg.getSender().getLocalName()
					.equals("approvisonnement")) {
				// TODO reception du msg de l'approvisonnement
				MessageApprovisonnement approv = null;
				try {
					approv = (MessageApprovisonnement) msg.getContentObject();
					MessageCommande com = approv.getCommande();
					// doWait(X); X temps pour fabriquer l'elt
					String s = approv.getCommande().getCommandes().get(0);
					String[] s1 = s.split(" ");
					if (s1[0].contains("banquette")) {
						this.banquette = this.MAX_STOCK_BANQUETTE;
					}
					if (s1[0].contains("fauteuil")) {
						this.fauteuil = this.MAX_STOCK_FAUTEUIL;
					}
					if (s1[0].contains("etagere")) {
						this.etagere = this.MAX_STOCK_ETAGERE;
					}

					this.envoiCommandeCommercial(com);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void traitementCommande(MessageCommande commandes) {
		ArrayList<String> c = commandes.getCommandes();
		for (String commande : c) {
			if (commande.contains("banquette")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeBanquette(com);
			}
			if (commande.contains("fauteuil")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeFauteuil(com);
			}
			if (commande.contains("etagere")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeEtagere(com);
			}
		}
	}

	private void traitementCommandeBanquette(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) < this.banquette) {
			// TODO envoi commande au commercial avec déc du nombre des tables
			// du stock
			this.banquette -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("noyer");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_BANQUETTE)
					* Atelier3Agent.PLANCHE_BANQUETTE);
			this.approNoyer(appro);
		}
	}

	private void traitementCommandeFauteuil(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) < this.fauteuil) {
			// TODO envoi commande au commercial avec déc du nombre des chaises
			// du stock
			this.fauteuil -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("noyer");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_FAUTEUIL)
					* Atelier3Agent.PLANCHE_FAUTEUIL);
			this.approNoyer(appro);
		}
	}

	private void traitementCommandeEtagere(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) <= this.etagere) {
			// TODO envoi commande au commercial avec déc du nombre des buffets
			// du stock
			this.etagere -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("noyer");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_ETAGERE)
					* Atelier3Agent.PLANCHE_ETAGERE);
			this.approNoyer(appro);

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

	public void approNoyer(MessageApprovisonnement commande) {
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
