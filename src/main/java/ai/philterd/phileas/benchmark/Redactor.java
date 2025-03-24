package ai.philterd.phileas.benchmark;

import ai.philterd.phileas.model.cache.InMemoryCache;
import ai.philterd.phileas.model.configuration.PhileasConfiguration;
import ai.philterd.phileas.model.enums.MimeType;
import ai.philterd.phileas.model.policy.Identifiers;
import ai.philterd.phileas.model.policy.Policy;
import ai.philterd.phileas.model.policy.filters.BankRoutingNumber;
import ai.philterd.phileas.model.policy.filters.BitcoinAddress;
import ai.philterd.phileas.model.policy.filters.CreditCard;
import ai.philterd.phileas.model.policy.filters.DriversLicense;
import ai.philterd.phileas.model.policy.filters.EmailAddress;
import ai.philterd.phileas.model.policy.filters.IbanCode;
import ai.philterd.phileas.model.policy.filters.PassportNumber;
import ai.philterd.phileas.model.policy.filters.PhoneNumber;
import ai.philterd.phileas.model.policy.filters.Ssn;
import ai.philterd.phileas.model.policy.filters.strategies.AbstractFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.BankRoutingNumberFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.BitcoinAddressFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.CreditCardFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.DriversLicenseFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.EmailAddressFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.IbanCodeFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.PassportNumberFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.PhoneNumberFilterStrategy;
import ai.philterd.phileas.model.policy.filters.strategies.rules.SsnFilterStrategy;
import ai.philterd.phileas.model.responses.FilterResponse;
import ai.philterd.phileas.model.services.CacheService;
import ai.philterd.phileas.services.PhileasFilterService;
import okhttp3.internal.cache.CacheStrategy;

import java.util.List;
import java.util.Properties;

/**
 * Single-threaded redactor using Phileas PII engine.
 */
public class Redactor {

    public Redactor(String name) throws Exception {
        boolean all = "mask_all".equals(name);
        boolean valid = "skip_all".equals(name);
        Identifiers identifiers = new Identifiers();

        if (all || "mask_bank_routing_numbers".equals(name)) {
            BankRoutingNumberFilterStrategy fs = new BankRoutingNumberFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            BankRoutingNumber x = new BankRoutingNumber();
            x.setBankRoutingNumberFilterStrategies(List.of(fs));
            identifiers.setBankRoutingNumber(x);
            valid = true;
        }

        if (all || "mask_bitcoin_addresses".equals(name)) {
            BitcoinAddressFilterStrategy fs = new BitcoinAddressFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            BitcoinAddress x = new BitcoinAddress();
            x.setBitcoinFilterStrategies(List.of(fs));
            identifiers.setBitcoinAddress(x);
            valid = true;
        }

        if (all || "mask_credit_cards".equals(name)) {
            CreditCardFilterStrategy fs = new CreditCardFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            CreditCard x = new CreditCard();
            x.setCreditCardFilterStrategies(List.of(fs));
            identifiers.setCreditCard(x);
            valid = true;
        }

        if (all || "mask_drivers_licenses".equals(name)) {
            DriversLicenseFilterStrategy fs = new DriversLicenseFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            DriversLicense x = new DriversLicense();
            x.setDriversLicenseFilterStrategies(List.of(fs));
            identifiers.setDriversLicense(x);
            valid = true;
        }

        if (all || "mask_email_addresses".equals(name)) {
            EmailAddressFilterStrategy fs = new EmailAddressFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            EmailAddress x = new EmailAddress();
            x.setEmailAddressFilterStrategies(List.of(fs));
            identifiers.setEmailAddress(x);
            valid = true;
        }

        if (all || "mask_iban_codes".equals(name)) {
            IbanCodeFilterStrategy fs = new IbanCodeFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            IbanCode x = new IbanCode();
            x.setIbanCodeFilterStrategies(List.of(fs));
            identifiers.setIbanCode(x);
            valid = true;
        }

        if (all || "mask_passport_numbers".equals(name)) {
            PassportNumberFilterStrategy fs = new PassportNumberFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            PassportNumber x = new PassportNumber();
            x.setPassportNumberFilterStrategies(List.of(fs));
            identifiers.setPassportNumber(x);
            valid = true;
        }

        if (all || "mask_phone_numbers".equals(name)) {
            PhoneNumberFilterStrategy fs = new PhoneNumberFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            PhoneNumber x = new PhoneNumber();
            x.setPhoneNumberFilterStrategies(List.of(fs));
            identifiers.setPhoneNumber(x);
            valid = true;
        }

        if (all || "mask_ssns".equals(name)) {
            SsnFilterStrategy fs = new SsnFilterStrategy();
            fs.setStrategy(AbstractFilterStrategy.MASK);
            fs.setMaskCharacter("*");
            fs.setMaskLength("same");
            Ssn x = new Ssn();
            x.setSsnFilterStrategies(List.of(fs));
            identifiers.setSsn(x);
            valid = true;
        }

        // quit if name parameter didn't match
        if (!valid) throw new IllegalArgumentException("Invalid redactor: " + name);

        // create filter service
        this.policy = new Policy();
        policy.setName("default");
        policy.setIdentifiers(identifiers);
        Properties properties = new Properties();
        PhileasConfiguration configuration = new PhileasConfiguration(properties, "phileas");
        CacheService cacheService = new InMemoryCache();
        this.filterService = new PhileasFilterService(configuration, cacheService);
    }

    private final PhileasFilterService filterService;
    private final Policy policy;

    public FilterResponse filter(String s) throws Exception {
        return filterService.filter(policy, "context_id", "document_id", s, MimeType.TEXT_PLAIN);
    }

}
