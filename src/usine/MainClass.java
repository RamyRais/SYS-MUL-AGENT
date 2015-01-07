package usine;

import java.util.ArrayList;

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
			AgentController atelier2 = mc.createNewAgent("atelier2",
					"usine.Atelier2Agent", null);
			AgentController atelier3 = mc.createNewAgent("atelier3",
					"usine.Atelier3Agent", null);
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
			AgentController fournisseurMerisier1 = mc.createNewAgent("fournisseur5",
					"usine.FournisseurMerisierAgent", null);
			AgentController fournisseurMerisier2 = mc.createNewAgent("fournisseur6",
					"usine.FournisseurMerisierAgent", null);
			AgentController fournisseurNoyer1 = mc.createNewAgent("fournisseur7",
					"usine.FournisseurNoyerAgent", null);
			AgentController fournisseurNoyer2 = mc.createNewAgent("fournisseur8",
					"usine.FournisseurNoyerAgent", null);
			AgentController fournisseurNoyer3 = mc.createNewAgent("fournisseur9",
					"usine.FournisseurNoyerAgent", null);
			ArrayList<String>  a1 = new ArrayList<String>(){{
				add("lit 10");
				add("banquette 30");
				add("chaise 20");
			}};
			ArrayList<String>  a2 = new ArrayList<String>(){{
				add("chevet 50");
				add("fauteuil 120");
				add("table 30");
			}};
			Object[] arg = new Object[1];
			arg[0] = a1;
			Object[] arg2 = new Object[1];
			arg2[0] = a2;
			AgentController client = mc.createNewAgent("client1",
					"usine.ClientAgent", arg);
			AgentController client2 = mc.createNewAgent("client2",
					"usine.ClientAgent", arg2);
			
			Object[] o = new Object[]{atelier1.getName(),
					commercial.getName(),client.getName(),approv.getName(),
					fournisseurChene1.getName(),fournisseurChene2.getName(),
					fournisseurChene3.getName(),fournisseurChene4.getName()};
			Object[] argS = new Object[1];
			argS[0] = o;
			AgentController sniffer = mc.createNewAgent("sniffer",
					"jade.tools.sniffer.Sniffer", argS);
			
			
			atelier1.start();
			atelier2.start();
			atelier3.start();
			commercial.start();
			approv.start();
			fournisseurChene1.start();
			fournisseurChene2.start();
			fournisseurChene3.start();
			fournisseurChene4.start();
			fournisseurMerisier1.start();
			fournisseurMerisier2.start();
			fournisseurNoyer1.start();
			fournisseurNoyer2.start();
			fournisseurNoyer3.start();
			client.start();
			client2.start();
			sniffer.start();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
