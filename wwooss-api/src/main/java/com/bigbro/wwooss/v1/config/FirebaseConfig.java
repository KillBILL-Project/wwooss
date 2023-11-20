package com.bigbro.wwooss.v1.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FirebaseConfig {

    @Value("${gcp.firebase.service-account}")
    private Resource serviceAccount;

    @Bean
    FirebaseApp firebaseSrApp() {
        return this.initializeFirebaseApp();
    }

    private FirebaseApp initializeFirebaseApp() {
        try( InputStream is = serviceAccount.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .build();
            return FirebaseApp.initializeApp(options, "wwooss");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
