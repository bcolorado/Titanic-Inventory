package com.titanicos.TitanicInventory;

import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@SpringBootApplication
@EnableMongoRepositories
public class TitanicInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TitanicInventoryApplication.class, args);
	}

	@Autowired
	UserRepository devRepo;

	@EventListener(ApplicationReadyEvent.class)
	public void checkDevAccount() throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("Checking for dev account...");
		User test = devRepo.findUserByID("developer");
		if (test == null) {
			System.out.println("Creating dev account...");
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			User devAcc = new User("developer", hash, salt, "Desarrollador", "administrador");
			devRepo.save(devAcc);
			System.out.println("Created dev account...");
		}else {
			System.out.println("dev account already exists...");
		}
		System.out.println("Application Ready");
	}

}
