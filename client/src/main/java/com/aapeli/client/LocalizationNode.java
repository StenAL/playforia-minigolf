package com.aapeli.client;

import com.aapeli.tools.Tools;
import com.aapeli.tools.XmlUnit;

class LocalizationNode {

    private String language;
    private String singular;
    private String plural;
    private String zero;
    private final TextManager textManager;

    protected LocalizationNode(TextManager textManager, String language, XmlUnit unit, boolean reversed) {
        this.textManager = textManager;
        this.language = language.substring(0, 2).toLowerCase();
        this.singular = unit.getChildValue("singular");
        this.plural = unit.getChildValue("plural");
        this.zero = unit.getChildValue("zero");
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

            if (this.plural != null && !this.language.equals("fr")) {
                return this.plural;
            }
        } else if ((quantity < 0 || quantity > 1) && this.plural != null) {
            return this.plural;
        }

        return this.singular;
    }
}
