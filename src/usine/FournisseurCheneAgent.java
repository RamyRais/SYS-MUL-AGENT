package usine;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class FournisseurCheneAgent extends Agent {

	protected void setup(){
		this.publierService("vente chene", "vente chene");
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
			//type+" chene "+quantite+" "+reciever
			String content = msg.getContent();
			this.envoiApprovisonnement(content, msg.getSender().getLocalName());
		}
	}
	
	public void envoiApprovisonnement(String content, String reciever){
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.addReceiver(new AID(reciever, AID.ISLOCALNAME));
		msg.setContent(content);
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
