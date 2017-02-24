package music.festival.passes;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bryce_fisher on 2/23/17.
 */
@RestController
@RequestMapping(value = "/list", method = RequestMethod.GET)
public class CurrentRegistrationsController {

    @Autowired
    private AccountRepository accountRepository;

    private static void setFilenameTimestamp(final HttpServletResponse httpServletResponse) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMDD_HHmmss");
        final String formattedDate = simpleDateFormat.format(new Date());
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + formattedDate + '.' + "csv" + "\"");
    }

    @GetMapping
    public void redirectToXML(final HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/list/xml");
    }

    @GetMapping(value = "/xml", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE})
    public List<Account> getAllXML() {
        return getAll();
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Account> getAllJSON() {
        return getAll();
    }

    @GetMapping(value = "/csv", produces = "text/csv")
    public void getAllCSV(final HttpServletResponse httpServletResponse) throws IOException {
        final List<Account> all = getAll();
        if (all.isEmpty()) {
            return;
        }
        setFilenameTimestamp(httpServletResponse);

        final CsvMapper csvMapper = new CsvMapper();
        final CsvSchema csvSchema = csvMapper.schemaFor(all.get(0).getClass()).withHeader();

        final ObjectWriter objectWriter = csvMapper.writer(csvSchema);
        objectWriter.writeValue(httpServletResponse.getOutputStream(), all);
    }

    private List<Account> getAll() {
        final List<Account> accounts = accountRepository.findByWristbandBadgeIdIsNotNull();
        if (accounts == null) {
            return new ArrayList<>(0);
        }

        return accounts.stream()
                .filter((account) -> StringUtils.hasText(account.getWristbandBadgeId()))
                .collect(Collectors.toList());
    }
}
