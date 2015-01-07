package usine;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ClientAgent extends Agent {
	
	protected void setup(){
		doWait(15000);
		Object param[] = new Object[1];

		//récupération des arguments de l'agent
		param = getArguments();
		MessageCommande commande = new MessageCommande();
		commande.setNomClient(this.getLocalName());
		commande.setCommandes((ArrayList<String>)param[0]);
//		ArrayList<String> c = new ArrayList<String>(){{
//			add("table 5");
//			add("lit 30");
//			add("banquette 5");
//			}};
//		commande.setCommandes(c);
		this.chercherService("vente meuble",commande);
	}
	
	public void passerCommande(){
		
	}
	
	public void enoyerMessage(String receiver, MessageCommande commande) {
		System.out.println("acheter meuble j'envoie la commande : "+commande);
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		try {
			msg.setContentObject(commande);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//msg.setContentObject(s)
		send(msg);
	}
	
	public void chercherService(String type, MessageCommande commande) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		dfd.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			this.enoyerMessage(result[0].getName().getLocalName(), commande);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
