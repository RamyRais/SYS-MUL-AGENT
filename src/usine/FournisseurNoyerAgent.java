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

public class FournisseurNoyerAgent extends Agent {

	protected void setup() {
		this.publierService("vente noyer", "vente noyer");
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// réception des messages
				recieveMessage();
			}
		});
	}

	public void recieveMessage() {
		ACLMessage msg = receive();
		if (msg != null) {
			try {
				MessageApprovisonnement approv = (MessageApprovisonnement) msg
						.getContentObject();
				// doWait(X); X temps pour préparer le bois
				this.envoiApprovisonnement(approv, msg.getSender()
						.getLocalName());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void envoiApprovisonnement(MessageApprovisonnement approv,
			String reciever) {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.addReceiver(new AID(reciever, AID.ISLOCALNAME));
		try {
			msg.setContentObject(approv);
			send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
