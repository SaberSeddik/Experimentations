data "azurerm_resource_group" "experimentation" {
  name = "experimentation"
}

resource "azurerm_container_group" "monitor_aci" {
  name = "experi-monitor-aci"

  resource_group_name = data.azurerm_resource_group.experimentation.name
  location = data.azurerm_resource_group.experimentation.location

  dns_name_label="experi-monitor-aci"
  os_type = "Linux"
  ip_address_type = "Public"

  container {
    name = "azure-monitor"
    image = "seddiks/experimentation:azure-monitor"
    cpu = 0.5
    memory = 1

    ports {
      port = 80
      protocol = "TCP"
    }
  }
  tags = {
    environment = "experimentation"
  }
}
