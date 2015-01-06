package usine;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ApprovisionnementAgent extends Agent {
	
	protected void setup(){
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// réception des messages
				recieveMessage();
			}
		});
	}
	
	public void recieveMessage(){
		ACLMessage msg = receive();
		if (msg != null) {
			MessageApprovisonnement appro;
			try {
				appro = (MessageApprovisonnement) msg.getContentObject();
			
			if(msg.getSender().getLocalName().contains("fournisseur")){
				System.out.println("jatni msg mil fournisseur");
				if(appro.getBois().contains("chene")){
					this.envoiAtelier(appro, "atelier1");
				}
				else if(appro.getBois().contains("merisier")){
					this.envoiAtelier(appro, "atelier2");
				}
				else if(appro.getBois().contains("noyer")){
					this.envoiAtelier(appro, "atelier3");
				}
			}else if(msg.getSender().getLocalName().contains("atelier")){
				//type+" chene "+quantite+" "+reciever
				if (appro.getBois().contains("chene")){
					//TODO recherche fournisseur de chene
					DFAgentDescription[] fournisseur = chercherService("vente chene");
					//TODO pour ameliorer ajouter un truc de random por choisir le fournisseur
					// ou on peut ajouter un truc de prix
					envoiFournisseur(appro,fournisseur[0].getName().getLocalName());
				}
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
	}
	
	public DFAgentDescription[] chercherService(String type) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void envoiFournisseur(MessageApprovisonnement appro, String reciever){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(reciever, AID.ISLOCALNAME));
		try {
			msg.setContentObject(appro);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(msg);
	}
	
	public void envoiAtelier(MessageApprovisonnement approv, String reciever){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(reciever, AID.ISLOCALNAME));
		try {
			msg.setContentObject(approv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(msg);
	}
}
