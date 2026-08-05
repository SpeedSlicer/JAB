package dev.speedslicer.api.permissions;

import java.util.HashMap;

public record PermGroup(String id, HashMap<String, ?> perms){
}
