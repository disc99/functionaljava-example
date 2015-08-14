package example.either;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fj.Effect;
import fj.F;
import fj.data.Either;
import fj.function.Effect1;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class EitherTest {

    @Test
    public void testEither_request_SuccessWhenSysout_FailWhenStackTrace() {
        final String url = "http://api.openweathermap.org/data/2.5/weather?q=Tokyo,jp";
        final Effect1<Exception> stackTrace = Exception::printStackTrace;
        final Effect1<JsonNode> sysout = System.out::println;

        get.f(url).right().bind(parse)
                .either(Effect.f(stackTrace), Effect.f(sysout));
    }

    // JsonデータをStringで取得
    F<String, Either<Exception, String>> get = urlString -> {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) new URL(urlString).getContent()));
            String line;
            final StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return Either.right(sb.toString());
        } catch (Exception e) {
            return Either.left(e);
        }
    };

    // StringのJsonデータをJsonNodeに変換
    F<String, Either<Exception, JsonNode>> parse = jsonString -> {
        try {
            return Either.right(new ObjectMapper().readValue(jsonString, JsonNode.class));
        } catch (Exception e) {
            return Either.left(e);
        }
    };
}

