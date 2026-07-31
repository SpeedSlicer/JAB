package dev.speedslicer.api.lootable;


import java.util.Map;
/**
 * @param rewards formatted as Item Name : Percentage Drop : Amount
 */
public record LootTableData (String id, Map<String, LootTableItemData> rewards){
}
