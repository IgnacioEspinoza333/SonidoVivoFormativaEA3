-- Agrega a la venta los datos de despacho que pide el checkout del frontend
ALTER TABLE venta
    ADD COLUMN region VARCHAR(255),
    ADD COLUMN direccion VARCHAR(255);