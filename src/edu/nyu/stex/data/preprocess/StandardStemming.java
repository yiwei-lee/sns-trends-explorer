package edu.nyu.stex.data.preprocess;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class StandardStemming {
  public static String stem(String str) {
    List<String> terms = stemToList(str);
    StringBuilder sb = new StringBuilder();
    for (String term : terms) {
      sb.append(term).append(" ");
    }
    return sb.toString();
  }

  public static List<String> stemToList(String str) {
    List<String> terms = new ArrayList<String>();

    if (str == null) {
      return terms;
    }

    Tokenizer tokenizer = new Tokenizer(new StringReader(str));

    // Add all terms appearing in content
    while (tokenizer.hasNext()) {
      String term = Tokenizer.lowercaseFilter(tokenizer.getText());
      // Delete the stop words for normal query terms
      term = Tokenizer.stopwordFilter(term);
      if (term != null) {
        term = Tokenizer.krovetzStemmerFilter(term);
        terms.add(term);
      }
    }
    return terms;
  }
}
