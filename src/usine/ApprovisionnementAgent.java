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
	
	public void envoiAtelier(String message,String reciever){
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.addReceiver(new AID(reciever, AID.ISLOCALNAME));
		msg.setContent(message);
		send(msg);
	}
	
	public void recieveMessage(){
		ACLMessage msg = receive();
		if (msg != null) {
			MessageApprovisonnement appro;
			try {
				appro = (MessageApprovisonnement) msg.getContentObject();
			
			if(msg.getSender().getLocalName().equals("fournisseur")){
				
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

}
