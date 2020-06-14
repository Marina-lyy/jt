package com.jt.service;

import java.util.List;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

public interface ItemCatService {

	ItemCat findItemCatNameById(Long itemCatId);

	List<EasyUITree> findItemCatList(Long parentId);

	List<EasyUITree> findItemCatCache(Long parentId);

	ItemCat findItemCatById(Long id);

	ItemCat updateItemCatById(ItemCat itemCat);

}
