# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: event.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x0b\x65vent.proto\x12\x0c\x65ventPackage\"\x07\n\x05\x45mpty\"\x82\x01\n\x17OnConditionSubscription\x12\x0e\n\x06\x63ities\x18\x01 \x03(\t\x12\"\n\x04size\x18\x02 \x01(\x0e\x32\x12.eventPackage.SizeH\x00\x12&\n\x06rating\x18\x03 \x01(\x0b\x32\x14.eventPackage.RatingH\x00\x42\x0b\n\tcondition\"8\n\x14PeriodicSubscription\x12\x10\n\x08interval\x18\x01 \x01(\r\x12\x0e\n\x06\x63ities\x18\x02 \x03(\t\"l\n\x05\x45vent\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x0c\n\x04\x63ity\x18\x02 \x01(\t\x12 \n\x04size\x18\x03 \x01(\x0e\x32\x12.eventPackage.Size\x12%\n\x07ratings\x18\x04 \x03(\x0b\x32\x14.eventPackage.Rating\"\x17\n\x06Rating\x12\r\n\x05value\x18\x01 \x01(\r*5\n\x04Size\x12\x0b\n\x07\x44\x45\x46\x41ULT\x10\x00\x12\t\n\x05SMALL\x10\x01\x12\n\n\x06MEDIUM\x10\x02\x12\t\n\x05LARGE\x10\x03\x32\xee\x01\n\x0c\x45ventService\x12P\n\x11SubscribePeriodic\x12\".eventPackage.PeriodicSubscription\x1a\x13.eventPackage.Event\"\x00\x30\x01\x12V\n\x14SubscribeOnCondition\x12%.eventPackage.OnConditionSubscription\x1a\x13.eventPackage.Event\"\x00\x30\x01\x12\x34\n\x04Ping\x12\x13.eventPackage.Empty\x1a\x13.eventPackage.Empty\"\x00\x30\x01\x62\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'event_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  _SIZE._serialized_start=364
  _SIZE._serialized_end=417
  _EMPTY._serialized_start=29
  _EMPTY._serialized_end=36
  _ONCONDITIONSUBSCRIPTION._serialized_start=39
  _ONCONDITIONSUBSCRIPTION._serialized_end=169
  _PERIODICSUBSCRIPTION._serialized_start=171
  _PERIODICSUBSCRIPTION._serialized_end=227
  _EVENT._serialized_start=229
  _EVENT._serialized_end=337
  _RATING._serialized_start=339
  _RATING._serialized_end=362
  _EVENTSERVICE._serialized_start=420
  _EVENTSERVICE._serialized_end=658
# @@protoc_insertion_point(module_scope)
