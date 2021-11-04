package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.*;

/** This is the Main class of the application. I parse the input file,
 * create the different components of the application, and run the system.
 * In the end, I output a Json.
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		Gson gson = new Gson();
		Input input = null;
		try {
			Reader reader = new FileReader(args [0]);
			input = gson.fromJson(reader, Input.class);
		}
		catch (Exception e) {
			System.out.println("File Not Found");
		}
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.setMembers(input.getEwoks());
		Diary diary = Diary.getInstance();
		Thread HanSoloThread = new Thread(new HanSoloMicroservice());
		Thread C3POThread = new Thread(new C3POMicroservice());
		Thread LeiaThread = new Thread(new LeiaMicroservice(input.getAttacks()));
		Thread R2D2Thread = new Thread(new R2D2Microservice(input.getR2D2()));
		Thread LandoThread = new Thread(new LandoMicroservice(input.getLando()));
		LeiaThread.start();
		HanSoloThread.start();
		C3POThread.start();
		R2D2Thread.start();
		LandoThread.start();
		try {
			HanSoloThread.join();
			C3POThread.join();
			LeiaThread.join();
			R2D2Thread.join();
			LandoThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try (FileWriter writer = new FileWriter(args [1])) {
			gson.toJson(diary, writer);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
