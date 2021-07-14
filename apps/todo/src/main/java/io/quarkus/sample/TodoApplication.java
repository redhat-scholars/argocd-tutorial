package io.quarkus.sample;

import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title="TODOS API",
        version = "1.0.0",
        contact = @Contact(
            name = "TODOS API Support",
            url = "http://todos.com/contact",
            email = "techsupport@todos.com"),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class TodoApplication extends Application {

}
