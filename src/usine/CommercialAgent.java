package usine;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

public class CommercialAgent extends Agent {

	protected void setup() {
		// TODO Auto-generated method stub
		this.publierService("vente meuble", "vente meuble");
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
			if(msg.getSender().getLocalName().contains("client")){
				MessageCommande commandes = null;
				try {
					commandes = (MessageCommande)	msg.getContentObject();
					this.traitementCommandeClient(commandes);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.getSender().getLocalName().contains("atelier")){
				MessageCommande commandes = null;
				try {
					commandes = (MessageCommande)	msg.getContentObject();
					this.commandeClientPrete(commandes);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	public void commandeClientPrete(MessageCommande commandes){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(commandes.getNomClient(),AID.ISLOCALNAME));
		ArrayList<String> com = commandes.getCommandes();
		String message = new String("la commande suivante est prête ");
		for (String s : com) {
			message = message + s + " ";
		}
		System.out.println(message);
		msg.setContent(message);
		send(msg);
	}
	
	public void traitementCommandeClient(MessageCommande commandes){
		ArrayList<String> c = commandes.getCommandes();
		ArrayList<String> atelier1 = new ArrayList<String>();
		ArrayList<String> atelier2 = new ArrayList<String>();
		ArrayList<String> atelier3 = new ArrayList<String>();
		for (String com : c) {
			if (com.contains("table")
					|| com.contains("chaise")
					|| com.contains("buffet")){
				atelier1.add(com);
			}else if(com.contains("lit")
					|| com.contains("chevet")
					|| com.contains("armoire")){
				atelier2.add(com);
			}else if(com.contains("banquette")
					|| com.contains("fauteuil")
					|| com.contains("etagere")){
				atelier3.add(com);
			}
		}
		if(!atelier1.isEmpty()){
			MessageCommande com = new MessageCommande();
			com.setNomClient(commandes.getNomClient());
			com.setCommandes(atelier1);
			this.envoiCommandeAtelier("atelier1",com);
		}
		if(!atelier2.isEmpty()){
			MessageCommande com = new MessageCommande();
			com.setNomClient(commandes.getNomClient());
			com.setCommandes(atelier2);
			this.envoiCommandeAtelier("atelier2",com);
		}
		if(!atelier3.isEmpty()){
			MessageCommande com = new MessageCommande();
			com.setNomClient(commandes.getNomClient());
			com.setCommandes(atelier3);
			this.envoiCommandeAtelier("atelier3",com);
		}
	}
	
	public void envoiCommandeAtelier(String atelier, MessageCommande commande){
		System.out.println(commande.toString());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(atelier,AID.ISLOCALNAME));
		try {
			msg.setContentObject(commande);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(msg);
	}
	
	public void publierService(String type, String name) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(getLocalName() + " " + name);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

	}
}
