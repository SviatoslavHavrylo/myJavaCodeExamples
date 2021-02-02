DROP PROCEDURE IF EXISTS `getPartDetailsToAdd`;

DELIMITER $$
CREATE PROCEDURE `getPartDetailsToAdd`(enterpriseId INT(11), partId INT(11), purchaseOrderNumber INT(11),
                                       startDate datetime)
BEGIN
    SELECT uuid()                               AS id,
           purchaseOrder.purchaseOrderId        AS purchaseOrderId,
           purchaseOrder.purchaseOrderNumber    AS purchaseOrderNumber,
           purchaseOrder.purchaseOrderStatusId  AS purchaseOrderStatusId,
           part.partId                          AS partId,
           part.partNumber                      AS partNumber,
           part.partDescription                 AS partDescription,
           part.partNumberDescription           AS partNumberDescription,
           part.partPrice                       AS partPrice,
           part.partTaxId                       AS partTaxId,
           part.partSurchargePartId             AS partSurchargePartId,
           purchaseOrder.supplierId             AS supplierId,
           purchaseOrder.supplierName           AS supplierName,
           purchaseOrder.supplierAcNo           AS supplierAcNo,
           purchaseOrder.supplierTaxId          AS supplierTaxId,
           purchaseOrder.supplierDeliveryCharge AS supplierDeliveryCharge,
           totallySold.totallySoldQuantity      AS totallySoldQuantity,
           lastOrder.lastOrderedDate            AS lastOrderedDate,
           lastSold.lastSoldDate                AS lastSoldDate
    FROM (SELECT popheader.id            AS purchaseOrderId,
                 popheader.PopNo         AS purchaseOrderNumber,
                 popheader.status        AS purchaseOrderStatusId,
                 profile.id              AS supplierId,
                 profile.Name            AS supplierName,
                 profile.Supplier_acno   AS supplierAcNo,
                 profile.tax_id          AS supplierTaxId,
                 profile.delivery_charge AS supplierDeliveryCharge
          FROM popheader
                   JOIN profile ON popheader.SuppId = profile.id
          WHERE popheader.enterpriseid = enterpriseId
            AND popheader.PopNo = purchaseOrderNumber) purchaseOrder
             JOIN (SELECT parts.id                                            AS partId,
                          parts.part_number                                   AS partNumber,
                          parts.description                                   AS partDescription,
                          CONCAT(parts.part_number, ' - ', parts.description) AS partNumberDescription,
                          parts.cost_price                                    AS partPrice,
                          parts.Purchase_tax_code_id                          AS partTaxId,
                          parts.surcharge_part_id                             AS partSurchargePartId
                   FROM parts
                   WHERE parts.id = partId) part
                  ON TRUE
             LEFT JOIN (SELECT MAX(partLastDate.lastSold) AS lastSoldDate
                        FROM (
                                 SELECT MAX(Date) as lastSold
                                 FROM job_parts
                                 WHERE parts_id = partId
                                   AND deleted = FALSE
                                 UNION ALL
                                 SELECT MAX(created_date)
                                 FROM sales_order_parts
                                 WHERE parts_id = partId
                                   AND deleted = FALSE) partLastDate) lastSold ON TRUE
             LEFT JOIN (SELECT MAX(popheader.PopDate) AS lastOrderedDate
                        FROM popdetails
                                 INNER JOIN popheader
                                            ON popdetails.enterpriseid = popheader.enterpriseid
                                                AND popdetails.PopNo = popheader.PopNo
                                 INNER JOIN parts
                                            ON popdetails.enterpriseid = parts.enterprises_id
                                                AND popdetails.PartNo = parts.part_number
                        WHERE parts.id = partId
                          AND popheader.Status <> 5) lastOrder ON TRUE
             LEFT JOIN (SELECT SUM(jobsAndSales.soldQty + jobsAndSales.jobQty) AS totallySoldQuantity
                        FROM (
                                 SELECT SUM(soParts.quantity) as soldQty, 0 as jobQty
                                 FROM sales_order_parts soParts
                                          INNER JOIN parts ON soParts.parts_id = parts.id
                                 WHERE parts.id = partId
                                   AND soParts.deleted = FALSE
                                   AND soParts.created_date >= startDate
                                 UNION ALL
                                 SELECT 0, SUM(job_parts.Quantity)
                                 FROM job_parts
                                          INNER JOIN parts ON job_parts.parts_id = parts.id
                                 WHERE parts.id = partId
                                   AND job_parts.Deleted = FALSE
                                   AND job_parts.Date >= startDate) jobsAndSales) totallySold ON TRUE;
END $$
DELIMITER ;
