package usine;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// récupération de l'instance de l'environnement JADE
		Runtime rt = Runtime.instance();
		// création d'un profil par défaut pour le conteneur principal
		Profile p = new ProfileImpl();
		// création d'un conteneur principal avec le profil par défaut
		AgentContainer mc = rt.createMainContainer(p);
		try {
			// création d'un RMA (Remote Management Agent)
			AgentController rma = mc.createNewAgent("rma",
					"jade.tools.rma.rma", null);
			// lancement du RMA
			rma.start();
			AgentController atelier1 = mc.createNewAgent("atelier1",
					"usine.Atelier1Agent", null);
			AgentController commercial = mc.createNewAgent("commercial",
					"usine.CommercialAgent", null);
			AgentController approv = mc.createNewAgent("approvisonnement",
					"usine.ApprovisionnementAgent", null);
			AgentController fournisseurChene1 = mc.createNewAgent("fournisseur1",
					"usine.FournisseurCheneAgent", null);
			AgentController fournisseurChene2 = mc.createNewAgent("fournisseur2",
					"usine.FournisseurCheneAgent", null);
			AgentController fournisseurChene3 = mc.createNewAgent("fournisseur3",
					"usine.FournisseurCheneAgent", null);
			AgentController fournisseurChene4 = mc.createNewAgent("fournisseur4",
					"usine.FournisseurCheneAgent", null);
			
			Object[] arg = new Object[2];
			arg[0] = "table";
			arg[1] = 5;
			AgentController client = mc.createNewAgent("client",
					"usine.ClientAgent", arg);
			Object[] o = new Object[]{atelier1.getName(),
					commercial.getName(),client.getName(),approv.getName(),
					fournisseurChene1.getName(),fournisseurChene2.getName(),
					fournisseurChene3.getName(),fournisseurChene4.getName()};
			Object[] argS = new Object[1];
			argS[0] = o;
			AgentController sniffer = mc.createNewAgent("sniffer",
					"jade.tools.sniffer.Sniffer", argS);
			
			
			atelier1.start();
			commercial.start();
			approv.start();
			fournisseurChene1.start();
			fournisseurChene2.start();
			fournisseurChene3.start();
			fournisseurChene4.start();
			client.start();
			sniffer.start();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
