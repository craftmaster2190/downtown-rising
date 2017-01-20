package music.festival.email;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by bryce_fisher on 1/20/17.
 */
@Service
public class EmailService {
    public void send(@NotNull String address, @NotNull String subject, @NotNull String body) {
        //TODO
    }

    public void send(@NotNull List<String> address, @NotNull String subject, @NotNull String body) {
        address.stream().forEach((a) -> send(a, subject, body));
    }
}
