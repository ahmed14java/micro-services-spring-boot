package com.photoappuser.amazon;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.photoappuser.dto.UserDto;

public class AmazonSES {

    final String FROM = "ahmed14java@gmail.com";

    final String SUBJECT = "One last step to complete your registration with OnyxApp";

    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "Thank your fro registering with out mobile app. To complete registering process and be able to log in click on the following link"
            + "<a href='http://localhost:8080/api/auth/email-verification?token=$tokenValue'> "
            + "Final step to complete your registration </a> <br /><br />"
            + "Thank you! And we are waiting for you inside!";

    final String TEXTBODY = "<h1>Please verify your email address</h1>"
            + "Thank your fro registering with out mobile app. To complete registering process and be able to log in click on the following link"
            + "<a href='http://localhost:8080/api/auth/email-verification?token=$tokenValue'> "
            + "Final step to complete your registration </a> <br /><br />"
            + "Thank you! And we are waiting for you inside!";

    public void verifyEmail(UserDto user){
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue" , user.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue" , user.getEmailVerificationToken());

        SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(user.getEmail()))
                                                         .withMessage(new Message()
                                                                          .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
                                                                                              .withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
                                                                          .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                                                         .withSource(FROM);
            client.sendEmail(request);
            System.out.println("Email Sent");
    }
}
