package com.aapeli.client;

import com.aapeli.tools.Tools;
import org.moparforia.shared.Language;
import org.moparforia.shared.Locale;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class LocalizationNode {

    private Language language;
    private String singular;
    private String plural;
    private String zero;

    protected LocalizationNode(Locale locale, Element element, boolean reversed) {
        this.language = locale.getLanguage();

        Node singular = element.getElementsByTagName("singular").item(0);
        if (singular != null) {
            this.singular = singular.getTextContent();
        }

        Node plural = element.getElementsByTagName("plural").item(0);
        if (plural != null) {
            this.plural = plural.getTextContent();
        }

        Node zero = element.getElementsByTagName("zero").item(0);
        if (zero != null) {
            this.zero = zero.getTextContent();
        }

        if (reversed) {
            this.singular = Tools.reverse(this.singular);
            this.plural = Tools.reverse(this.plural);
            this.zero = Tools.reverse(this.zero);
        }
    }

    protected String getLocalization(int quantity) {
        if (quantity == 0) {
            if (this.zero != null) {
                return this.zero;
            }

            if (this.plural != null && !this.language.equals(Language.FRENCH)) {
                return this.plural;
            }
        } else if ((quantity < 0 || quantity > 1) && this.plural != null) {
            return this.plural;
        }

        return this.singular;
    }
}
