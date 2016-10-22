use scm;
db.complaint_type.remove({});
db.complaint_type.insert({
"code": "CWSLA",
"complaintCategory": "Courier within SLA"
});
db.complaint_type.insert({
"code": "PSAC",
"complaintCategory": "Product stuck at courier"
});
db.complaint_type.insert({
"code": "AWBNT",
"complaintCategory": "AWB number not traceable",
});
db.complaint_type.insert({
"code": "RCNP",
"complaintCategory": "Requested cancellation of non-cancellable product"
});
db.complaint_type.insert({
"code": "WPD",
"complaintCategory": "Wrong Product Delivered"
});
db.complaint_type.insert({
"code": "PDWP",
"complaintCategory": "Product Delivered to Wrong Place"
});
db.complaint_type.insert({
"code": "PDP",
"complaintCategory": "Physical Damage in Product"
});
db.complaint_type.insert({
"code": "EPR",
"complaintCategory": "Empty parcel received"
});
db.complaint_type.insert({
"code": "POR",
"complaintCategory": "Only Partial order received"
});