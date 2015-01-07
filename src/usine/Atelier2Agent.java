package usine;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

public class Atelier2Agent extends Agent {

	private static final int MAX_STOCK_LIT = 20;
	private static final int MAX_STOCK_CHEVET = 120;
	private static final int MAX_STOCK_ARMOIRE = 20;
	private static final int PLANCHE_LIT = 5;
	private static final int PLANCHE_CHEVET = 2;
	private static final int PLANCHE_ARMOIRE = 6;
	private int lit;
	private int chevet;
	private int armoire;

	protected void setup() {
		this.lit = MAX_STOCK_LIT;
		this.chevet = MAX_STOCK_CHEVET;
		this.armoire = MAX_STOCK_ARMOIRE;
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
					if (s1[0].contains("lit")) {
						this.lit = this.MAX_STOCK_LIT;
					}
					if (s1[0].contains("chevet")) {
						this.chevet = this.MAX_STOCK_CHEVET;
					}
					if (s1[0].contains("armoire")) {
						this.armoire = this.MAX_STOCK_ARMOIRE;
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
			if (commande.contains("lit")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeLit(com);
			}
			if (commande.contains("chevet")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeChevet(com);
			}
			if (commande.contains("armoire")) {
				MessageCommande com = new MessageCommande();
				com.setNomClient(commandes.getNomClient());
				ArrayList<String> a = new ArrayList<String>();
				a.add(commande);
				com.setCommandes(a);
				this.traitementCommandeArmoire(com);
			}
		}
	}

	private void traitementCommandeLit(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) < this.lit) {
			// TODO envoi commande au commercial avec déc du nombre des tables
			// du stock
			this.lit -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("merisier");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_LIT)
					* Atelier2Agent.PLANCHE_LIT);
			this.approMerisier(appro);
		}
	}

	private void traitementCommandeChevet(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) < this.chevet) {
			// TODO envoi commande au commercial avec déc du nombre des chaises
			// du stock
			this.chevet -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("merisier");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_CHEVET)
					* Atelier2Agent.PLANCHE_CHEVET);
			this.approMerisier(appro);
		}
	}

	private void traitementCommandeArmoire(MessageCommande commandes) {
		ArrayList<String> com = commandes.getCommandes();
		String[] s = com.get(0).split(" ");
		if (Integer.parseInt(s[1]) <= this.armoire) {
			// TODO envoi commande au commercial avec déc du nombre des buffets
			// du stock
			this.armoire -= Integer.parseInt(s[1]);
			this.envoiCommandeCommercial(commandes);
		} else {
			// TODO envoi msg lil approvi bech na5o sel3a wne5dem
			MessageApprovisonnement appro = new MessageApprovisonnement();
			appro.setCommande(commandes);
			appro.setBois("merisier");
			appro.setQuantite(Integer.parseInt(s[1] + this.MAX_STOCK_ARMOIRE)
					* Atelier2Agent.PLANCHE_ARMOIRE);
			this.approMerisier(appro);

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

	public void approMerisier(MessageApprovisonnement commande) {
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
