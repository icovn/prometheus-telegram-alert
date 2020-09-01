package com.github.mikesafonov.prometheus.telegram.service.message;

import com.github.mikesafonov.prometheus.alerts.starter.dto.Alert;
import com.github.mikesafonov.prometheus.alerts.starter.dto.AlertManagerNotification;
import com.github.mikesafonov.prometheus.alerts.starter.dto.enums.AlertLevel;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomMessageConverter implements MessageConverter {
  private final EmojiService emojiService;
  
  @Override
  public String convert(AlertManagerNotification notification, Alert alert) {
    Optional<AlertLevel> level = alert.getLevel();
    String emojiByLevel = emojiService.emojiByLevel(level.orElse(AlertLevel.WARNING));
  
    String prefix = emojiByLevel + " * " + alert.getLabels().get("alertname");
  
    String message = prefix + " (" + alert.getStatus().name().toUpperCase() + "):*\n";
    if(alert.getStatus().name().equals("firing")){
      String description = alert.getAnnotations().get("description");
      if(description == null){
        description = alert.getAnnotations().get("descriptions");
      }
    
      message += "- DESCRIPTION: " + description+ "\n";
      if(alert.getStartsAt() != null){
        message += "- START AT: " + alert.getStartsAt() + "\n";
      }
    }else {
      if(alert.getAnnotations().get("summary") != null){
        message += "- SUMMARY: " + alert.getAnnotations().get("summary") + "\n";
      }
      if(alert.getStartsAt() != null){
        message += "- START AT: " + alert.getStartsAt() + "\n";
      }
      if(alert.getEndsAt() != null){
        message += "- END AT: " + alert.getEndsAt() + "\n";
      }
    }
  
    return message;
  }
}
