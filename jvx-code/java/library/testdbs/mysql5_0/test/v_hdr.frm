TYPE=VIEW
query=select `test`.`hdr`.`ID` AS `ID`,`test`.`hdr`.`COMP_CODE` AS `COMP_CODE`,`test`.`hdr`.`ACCT_YEAR` AS `ACCT_YEAR`,`test`.`hdr`.`TRN_TYPE` AS `TRN_TYPE`,`test`.`hdr`.`DOC_NO` AS `DOC_NO`,`test`.`hdr`.`TRN_DT` AS `TRN_DT`,`test`.`hdr`.`PARTY` AS `PARTY`,`test`.`hdr`.`DOCTOR` AS `DOCTOR`,`test`.`hdr`.`TOT_TAX` AS `TOT_TAX`,`test`.`hdr`.`AMT_WO_TAX` AS `AMT_WO_TAX`,`test`.`hdr`.`AMOUNT` AS `AMOUNT`,`test`.`hdr`.`DISC` AS `DISC`,`test`.`hdr`.`SP_DISC` AS `SP_DISC`,`test`.`hdr`.`ADJUSTMENT` AS `ADJUSTMENT`,`test`.`hdr`.`C_NOTE` AS `C_NOTE`,`test`.`hdr`.`NET_AMOUNT` AS `NET_AMOUNT`,`test`.`hdr`.`AMOUNT_PAID` AS `AMOUNT_PAID`,`test`.`hdr`.`AMOUN_DUE` AS `AMOUN_DUE`,`test`.`hdr`.`NARRATION` AS `NARRATION`,`test`.`hdr`.`PAYMENT_DISC` AS `PAYMENT_DISC`,`test`.`hdr`.`P_DOC_NO` AS `P_DOC_NO`,`test`.`hdr`.`P_DT` AS `P_DT`,`test`.`hdr`.`STOCK` AS `STOCK`,`test`.`hdr`.`TAXONFQTY` AS `TAXONFQTY`,`test`.`party`.`PARTY_DESC` AS `PARTY_DESC`,`test`.`party`.`ADDRESS` AS `ADDRESS`,`test`.`doctors`.`DOCT_NAME` AS `DOCT_NAME` from ((`test`.`hdr` left join `test`.`party` on((`test`.`hdr`.`PARTY` = `test`.`party`.`PARTY`))) left join `test`.`doctors` on((`test`.`hdr`.`DOCTOR` = `test`.`doctors`.`ID`))) order by `test`.`hdr`.`TRN_DT` desc limit 200
md5=8ab6205356dd81da6aea4f036018d8d1
updatable=0
algorithm=0
definer_user=root
definer_host=localhost
suid=2
with_check_option=0
timestamp=2010-06-13 09:13:51
create-version=1
source=select `test`.`hdr`.`ID` AS `ID`,
client_cs_name=utf8
connection_cl_name=utf8_general_ci
view_body_utf8=select `test`.`hdr`.`ID` AS `ID`,`test`.`hdr`.`COMP_CODE` AS `COMP_CODE`,`test`.`hdr`.`ACCT_YEAR` AS `ACCT_YEAR`,`test`.`hdr`.`TRN_TYPE` AS `TRN_TYPE`,`test`.`hdr`.`DOC_NO` AS `DOC_NO`,`test`.`hdr`.`TRN_DT` AS `TRN_DT`,`test`.`hdr`.`PARTY` AS `PARTY`,`test`.`hdr`.`DOCTOR` AS `DOCTOR`,`test`.`hdr`.`TOT_TAX` AS `TOT_TAX`,`test`.`hdr`.`AMT_WO_TAX` AS `AMT_WO_TAX`,`test`.`hdr`.`AMOUNT` AS `AMOUNT`,`test`.`hdr`.`DISC` AS `DISC`,`test`.`hdr`.`SP_DISC` AS `SP_DISC`,`test`.`hdr`.`ADJUSTMENT` AS `ADJUSTMENT`,`test`.`hdr`.`C_NOTE` AS `C_NOTE`,`test`.`hdr`.`NET_AMOUNT` AS `NET_AMOUNT`,`test`.`hdr`.`AMOUNT_PAID` AS `AMOUNT_PAID`,`test`.`hdr`.`AMOUN_DUE` AS `AMOUN_DUE`,`test`.`hdr`.`NARRATION` AS `NARRATION`,`test`.`hdr`.`PAYMENT_DISC` AS `PAYMENT_DISC`,`test`.`hdr`.`P_DOC_NO` AS `P_DOC_NO`,`test`.`hdr`.`P_DT` AS `P_DT`,`test`.`hdr`.`STOCK` AS `STOCK`,`test`.`hdr`.`TAXONFQTY` AS `TAXONFQTY`,`test`.`party`.`PARTY_DESC` AS `PARTY_DESC`,`test`.`party`.`ADDRESS` AS `ADDRESS`,`test`.`doctors`.`DOCT_NAME` AS `DOCT_NAME` from ((`test`.`hdr` left join `test`.`party` on((`test`.`hdr`.`PARTY` = `test`.`party`.`PARTY`))) left join `test`.`doctors` on((`test`.`hdr`.`DOCTOR` = `test`.`doctors`.`ID`))) order by `test`.`hdr`.`TRN_DT` desc limit 200