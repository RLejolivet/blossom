package fr.blossom.core.common.entity.converter;

import java.util.Locale;
import javax.persistence.AttributeConverter;

/**
 * Custom AttributeConverter to store {@link Locale} into a database column.<br/>
 * It relies on {@link Locale#toLanguageTag()} to provide the locale as a {@code String}.
 *
 * @author Maël Gargadennnec
 */
public class LocaleConverter implements AttributeConverter<Locale, String> {

  @Override
  public String convertToDatabaseColumn(Locale locale) {
    if (locale != null) {
      return locale.toLanguageTag();
    }
    return null;
  }

  @Override
  public Locale convertToEntityAttribute(String languageTag) {
    if (languageTag != null && !languageTag.isEmpty()) {
      return Locale.forLanguageTag(languageTag);
    }
    return null;
  }
}