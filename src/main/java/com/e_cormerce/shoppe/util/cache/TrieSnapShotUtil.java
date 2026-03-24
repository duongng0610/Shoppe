package com.e_cormerce.shoppe.util.cache;

import com.e_cormerce.shoppe.cache.TernaryTrie;
import com.e_cormerce.shoppe.properties.SnapShotProperties;
import java.io.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TrieSnapShotUtil {
  SnapShotProperties snapShotProperties;

  public void save(TernaryTrie ternaryTrie) {
    save(ternaryTrie, snapShotProperties.getFilePath());
  }

  private void save(TernaryTrie ternaryTrie, String filePath) {
    try {

      FileOutputStream file = new FileOutputStream(filePath);
      BufferedOutputStream buffer = new BufferedOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(buffer);
      oos.writeObject(ternaryTrie);

    } catch (FileNotFoundException e) {

      throw new RuntimeException("File save for ternary trie is not found");
    } catch (IOException e) {
      throw new RuntimeException("Save ternary trie is failed");
    }
  }

  public TernaryTrie load() {
    File file = new File(snapShotProperties.getFilePath());

    if (!file.exists()) {
      throw new RuntimeException("Can not find file to load ternary trie");
    }

    try {
      FileInputStream fileInputStream = new FileInputStream(snapShotProperties.getFilePath());
      BufferedInputStream buffer = new BufferedInputStream(fileInputStream);
      ObjectInputStream ois = new ObjectInputStream(buffer);
      TernaryTrie ternaryTrie = (TernaryTrie) ois.readObject();
      return ternaryTrie;
    } catch (IOException | ClassNotFoundException e) {
      boolean deleted = file.delete();
      return null;
    }
  }
}
