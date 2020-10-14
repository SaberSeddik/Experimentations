data "azurerm_resource_group" "experimentation" {
  name = "experimentation"
}

resource "azurerm_container_group" "monitor_aci" {
  location = data.azurerm_resource_group.experimentation.location
  name = "experi-monitor-aci"
  os_type = "linux"
  resource_group_name = data.azurerm_resource_group.experimentation.name
  container {
    name = "hello-world"
    image = "microsoft/aci-helloworld:latest"
    cpu = 0.5
    memory = 1
  }
  tags = {
    environment = "experimentation"
  }
}
