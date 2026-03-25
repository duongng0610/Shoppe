package com.e_cormerce.shoppe.cache;

import com.e_cormerce.shoppe.dto.common.search.CategoryDto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class TernaryTrie implements Serializable {
    private TSTNode root;

    public void add(String word, String val) {
        root = add(word, val, root, 0);
    }

    private TSTNode add(String word, String val, TSTNode cur, int d) {
        char c = word.charAt(d);

        if (cur == null) cur = new TSTNode(c);

        if (c < cur.c) cur.left = add(word, val, cur.left, d);
        else if (c > cur.c) cur.right = add(word, val, cur.right, d);
        else if (d < word.length() - 1) cur.mid = add(word, val, cur.mid, d + 1);
        else cur.val = val;

        return cur;
    }

    public Iterable<CategoryDto> searchWithPrefix(String prefix) {
        Queue<CategoryDto> q = new LinkedList<>();
        TSTNode x = get(root, prefix, 0);
        if (x == null) return q;

        if (x.val != null) {
            CategoryDto projection =
                    CategoryDto.builder().val(prefix).id(x.val).build();
            q.add(projection);
        }

        collect(x.mid, prefix, q);
        return q;
    }

    private void collect(TSTNode cur, String prefix, Queue<CategoryDto> q) {
        if (cur == null) return;

        collect(cur.left, prefix, q);

        if (cur.val != null) {
            q.add(CategoryDto.builder().val(prefix + cur.c).id(cur.val).build());
        }

        collect(cur.mid, prefix + cur.c, q);

        collect(cur.right, prefix, q);
    }

    private TSTNode get(TSTNode cur, String word, int d) {
        if (cur == null) return cur;

        char c = word.charAt(d);

        if (c < cur.c) return get(cur.left, word, d);
        else if (c > cur.c) return get(cur.right, word, d);
        else if (d < word.length() - 1) return get(cur.mid, word, d + 1);
        else return cur;
    }

    public void delete(String word) {
        root = delete(root, word, 0);
    }

    private TSTNode delete(TSTNode cur, String word, int d) {
        if (cur == null) return cur;

        char c = word.charAt(d);
        if (c < cur.c) cur.left = delete(cur.left, word, d);
        else if (c > cur.c) cur.right = delete(cur.right, word, d);
        else if (d < word.length() - 1) cur.mid = delete(cur.mid, word, d + 1);
        else cur.val = null;

        if (cur.val == null && cur.mid == null) {
            if (cur.left != null) {
                cur = cur.left;
                cur.left = null;
            } else if (cur.right != null) {
                cur = cur.right;
                cur.right = null;
            } else cur = null;
        }

        return cur;
    }
}
