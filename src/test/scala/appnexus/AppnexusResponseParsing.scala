package api

import org.scalatest.{Assertions, FreeSpec}
import util.JsonUtils

/**
 * Created by anand on 10/11/14.
 */
class AppnexusResponseParsing extends FreeSpec with Assertions {

  "Appnexus parse advertiser service response" - {
    "parse advertiser_id(s) from advertiser service json response" in {
      val advertiserResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 2,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "advertisers": [
          |      {
          |        "id": 90781,
          |        "code": null,
          |        "name": "Vitalii's Advertiser 001",
          |        "state": "active",
          |        "default_brand_id": null,
          |        "remarketing_segment_id": null,
          |        "profile_id": 300628,
          |        "control_pct": 0,
          |        "timezone": "EST5EDT",
          |        "last_modified": "2013-12-12 21:13:45",
          |        "billing_name": null,
          |        "billing_phone": null,
          |        "billing_address1": "",
          |        "billing_address2": "",
          |        "billing_city": "",
          |        "billing_state": "",
          |        "billing_country": "",
          |        "billing_zip": "",
          |        "default_currency": "USD",
          |        "use_insertion_orders": false,
          |        "time_format": "12-hour",
          |        "is_malicious": false,
          |        "is_mediated": false,
          |        "billing_internal_user": null,
          |        "default_category": null,
          |        "default_brand": null,
          |        "labels": null,
          |        "competitive_brands": null,
          |        "competitive_categories": null,
          |        "lifetime_budget": null,
          |        "lifetime_budget_imps": null,
          |        "daily_budget": null,
          |        "daily_budget_imps": null,
          |        "enable_pacing": null,
          |        "lifetime_pacing": null,
          |        "lifetime_pacing_span": null,
          |        "allow_safety_pacing": null,
          |        "stats": null
          |      },
          |      {
          |        "id": 91414,
          |        "code": null,
          |        "name": "Mark's Advertiser (DO NOT USE)",
          |        "state": "active",
          |        "default_brand_id": null,
          |        "remarketing_segment_id": null,
          |        "profile_id": 316690,
          |        "control_pct": 0,
          |        "timezone": "EST5EDT",
          |        "last_modified": "2014-01-03 20:26:34",
          |        "billing_name": null,
          |        "billing_phone": null,
          |        "billing_address1": "",
          |        "billing_address2": "",
          |        "billing_city": "",
          |        "billing_state": "",
          |        "billing_country": "",
          |        "billing_zip": "",
          |        "default_currency": "USD",
          |        "use_insertion_orders": false,
          |        "time_format": "12-hour",
          |        "is_malicious": false,
          |        "is_mediated": false,
          |        "billing_internal_user": null,
          |        "default_category": null,
          |        "default_brand": null,
          |        "labels": null,
          |        "competitive_brands": null,
          |        "competitive_categories": null,
          |        "lifetime_budget": null,
          |        "lifetime_budget_imps": null,
          |        "daily_budget": null,
          |        "daily_budget_imps": null,
          |        "enable_pacing": null,
          |        "lifetime_pacing": null,
          |        "lifetime_pacing_span": null,
          |        "allow_safety_pacing": null,
          |        "stats": null
          |      }
          |    ],
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 2,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 2,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": true,
          |      "warnings": [],
          |      "time": 78.886985778809,
          |      "start_microtime": 1415550940.8155,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "advertisers"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseAdvertiserIds(advertiserResponseJson) === List(90781, 91414))
    }

    "when no advertisers are returned in advertiser service json response " in {
      val advertiserResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 2,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "advertisers": [],
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 2,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 2,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": true,
          |      "warnings": [],
          |      "time": 78.886985778809,
          |      "start_microtime": 1415550940.8155,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "advertisers"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseAdvertiserIds(advertiserResponseJson) === List())
    }

    "when HttpResponse string is empty from advertisers service json response " in {
      val advertiserResponseJson = """{}""".stripMargin
      assert(JsonUtils.parseAdvertiserIds(advertiserResponseJson) === List())
    }
  }

  "Appnexus parse campaign service response" - {
    "parse profile_id(s) from campaign service json response" in {
      val campaignResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 2,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "campaigns": [
          |      {
          |        "id": 386536,
          |        "state": "active",
          |        "code": null,
          |        "advertiser_id": 90781,
          |        "line_item_id": 167877,
          |        "creative_id": 694444,
          |        "pixel_id": null,
          |        "short_name": null,
          |        "name": "Vitalii's line item number 1: 31.10.13 this is tryTest App 002_test.VitaliisAdvertiser001_3",
          |        "profile_id": 302086,
          |        "start_date": "2013-12-12 17:32:00",
          |        "end_date": "2014-02-28 23:59:00",
          |        "timezone": "EST5EDT",
          |        "priority": 5,
          |        "cadence_modifier_enabled": false,
          |        "cpc_goal": null,
          |        "cpm_bid_type": "clearing",
          |        "base_bid": null,
          |        "min_bid": null,
          |        "max_bid": 40,
          |        "bid_margin": 0,
          |        "roadblock_creatives": false,
          |        "roadblock_type": "no_roadblock",
          |        "inventory_type": "real_time",
          |        "last_modified": "2013-12-12 22:22:13",
          |        "max_learn_bid": null,
          |        "cadence_type": "advertiser",
          |        "click_url": null,
          |        "require_cookie_for_tracking": true,
          |        "allow_unverified_ecp": false,
          |        "defer_to_li_prediction": false,
          |        "ecp_learn_divisor": null,
          |        "projected_learn_events": null,
          |        "confidence_threshold": null,
          |        "learn_threshold": null,
          |        "cpc_payout": null,
          |        "comments": null,
          |        "optimization_lever_mode": "automatic",
          |        "optimization_version": "v7",
          |        "learn_override_type": null,
          |        "base_cpm_bid_value": null,
          |        "impression_limit": 40000,
          |        "bid_multiplier": 1,
          |        "is_malicious": false,
          |        "remaining_days": null,
          |        "total_days": 79,
          |        "supply_type": null,
          |        "creatives": [
          |          {
          |            "id": 694444,
          |            "mapping_state": "active",
          |            "code": null,
          |            "name": "VitaliisAdvertiser001_300x250_test.cat",
          |            "width": 300,
          |            "height": 250,
          |            "state": "active",
          |            "audit_status": "pending",
          |            "is_expired": false,
          |            "is_prohibited": false,
          |            "is_self_audited": false,
          |            "format": "iframe-html",
          |            "pop_window_maximize": null
          |          }
          |        ],
          |        "pixels": null,
          |        "optimization_lookback": null,
          |        "campaign_modifiers": null,
          |        "labels": null,
          |        "broker_fees": null,
          |        "valuation": null,
          |        "lifetime_budget": null,
          |        "lifetime_budget_imps": 1257,
          |        "daily_budget": null,
          |        "daily_budget_imps": 15,
          |        "enable_pacing": true,
          |        "lifetime_pacing": false,
          |        "lifetime_pacing_span": null,
          |        "allow_safety_pacing": true
          |      },
          |      {
          |        "id": 389165,
          |        "state": "active",
          |        "code": null,
          |        "advertiser_id": 90781,
          |        "line_item_id": 167877,
          |        "creative_id": 697076,
          |        "pixel_id": null,
          |        "short_name": null,
          |        "name": "Vitalii's line item number 1: 31.10.13 this is tryTest App 004_test.VitaliisAdvertiser001_4",
          |        "profile_id": 315443,
          |        "start_date": "2013-12-16 17:10:00",
          |        "end_date": "2014-02-28 23:59:00",
          |        "timezone": "EST5EDT",
          |        "priority": 5,
          |        "cadence_modifier_enabled": false,
          |        "cpc_goal": null,
          |        "cpm_bid_type": "clearing",
          |        "base_bid": null,
          |        "min_bid": null,
          |        "max_bid": 40,
          |        "bid_margin": 0,
          |        "roadblock_creatives": false,
          |        "roadblock_type": "no_roadblock",
          |        "inventory_type": "real_time",
          |        "last_modified": "2013-12-16 22:00:39",
          |        "max_learn_bid": null,
          |        "cadence_type": "advertiser",
          |        "click_url": null,
          |        "require_cookie_for_tracking": true,
          |        "allow_unverified_ecp": false,
          |        "defer_to_li_prediction": false,
          |        "ecp_learn_divisor": null,
          |        "projected_learn_events": null,
          |        "confidence_threshold": null,
          |        "learn_threshold": null,
          |        "cpc_payout": null,
          |        "comments": null,
          |        "optimization_lever_mode": "automatic",
          |        "optimization_version": "v7",
          |        "learn_override_type": null,
          |        "base_cpm_bid_value": null,
          |        "impression_limit": 40000,
          |        "bid_multiplier": 1,
          |        "is_malicious": false,
          |        "remaining_days": null,
          |        "total_days": 75,
          |        "supply_type": null,
          |        "creatives": [
          |          {
          |            "id": 697076,
          |            "mapping_state": "active",
          |            "code": null,
          |            "name": "VitaliisAdvertiser001_300x250_test.cat",
          |            "width": 300,
          |            "height": 250,
          |            "state": "active",
          |            "audit_status": "pending",
          |            "is_expired": false,
          |            "is_prohibited": false,
          |            "is_self_audited": false,
          |            "format": "iframe-html",
          |            "pop_window_maximize": null
          |          }
          |        ],
          |        "pixels": null,
          |        "optimization_lookback": null,
          |        "campaign_modifiers": null,
          |        "labels": null,
          |        "broker_fees": null,
          |        "valuation": null,
          |        "lifetime_budget": null,
          |        "lifetime_budget_imps": 1244,
          |        "daily_budget": null,
          |        "daily_budget_imps": 16,
          |        "enable_pacing": true,
          |        "lifetime_pacing": false,
          |        "lifetime_pacing_span": null,
          |        "allow_safety_pacing": true
          |      }
          |    ],
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 1,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 1,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": false,
          |      "warnings": [],
          |      "time": 310.06598472595,
          |      "start_microtime": 1415608581.3044,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "campaigns"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseProfileIds(campaignResponseJson) === List(302086, 315443))
    }

    "when no campaigns are returned in campaign service json response" in {
      val campaignResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 0,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "campaigns": [],
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 1,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 1,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": false,
          |      "warnings": [],
          |      "time": 310.06598472595,
          |      "start_microtime": 1415608581.3044,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "campaigns"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseProfileIds(campaignResponseJson) === List())
    }

    "when HttpResponse string is empty from advertisers service json response" in {
      val campaignResponseJson =
        """{}""".stripMargin

      assert(JsonUtils.parseProfileIds(campaignResponseJson) === List())
    }
  }

  "Appnexus parse campaign service response" - {

    "parse segment names profile service json response" in {
      val profileResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 1,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "profile": {
          |      "id": 302086,
          |      "code": null,
          |      "description": null,
          |      "country_action": "include",
          |      "region_action": "exclude",
          |      "city_action": "exclude",
          |      "browser_action": "exclude",
          |      "use_inventory_attribute_targets": false,
          |      "last_modified": "2013-12-12 22:22:13",
          |      "daypart_timezone": null,
          |      "dma_action": "exclude",
          |      "domain_action": "exclude",
          |      "domain_list_action": "include",
          |      "inventory_action": "exclude",
          |      "language_action": "exclude",
          |      "segment_boolean_operator": "or",
          |      "min_session_imps": null,
          |      "session_freq_type": "platform",
          |      "carrier_action": "exclude",
          |      "supply_type_action": "exclude",
          |      "device_type_action": "exclude",
          |      "screen_size_action": "exclude",
          |      "device_model_action": "exclude",
          |      "location_target_radius": null,
          |      "location_target_latitude": null,
          |      "location_target_longitude": null,
          |      "querystring_action": "exclude",
          |      "querystring_boolean_operator": "and",
          |      "is_expired": false,
          |      "non_audited_url_action": "include",
          |      "advertiser_id": 90781,
          |      "publisher_id": null,
          |      "max_session_imps": null,
          |      "max_day_imps": 3,
          |      "max_lifetime_imps": null,
          |      "max_page_imps": null,
          |      "min_minutes_per_imp": null,
          |      "venue_action": "exclude",
          |      "operating_system_action": "exclude",
          |      "require_cookie_for_freq_cap": true,
          |      "trust": "seller",
          |      "allow_unaudited": false,
          |      "is_template": false,
          |      "created_on": "2013-12-12 22:22:13",
          |      "operating_system_family_action": "exclude",
          |      "use_operating_system_extended_targeting": false,
          |      "mobile_app_instance_action_include": false,
          |      "mobile_app_instance_list_action_include": false,
          |      "user_group_targets": null,
          |      "country_targets": [
          |        {
          |          "country": "NL",
          |          "name": "Netherlands"
          |        }
          |      ],
          |      "region_targets": null,
          |      "city_targets": null,
          |      "inv_class_targets": null,
          |      "inventory_source_targets": null,
          |      "inventory_attribute_targets": null,
          |      "age_targets": null,
          |      "daypart_targets": null,
          |      "browser_targets": null,
          |      "browser_family_targets": null,
          |      "dma_targets": null,
          |      "domain_targets": null,
          |      "domain_list_targets": [
          |        {
          |          "id": 1351,
          |          "name": "CPL",
          |          "description": "Certified publishers list",
          |          "type": "white",
          |          "deleted": false
          |        }
          |      ],
          |      "language_targets": null,
          |      "size_targets": null,
          |      "zip_targets": null,
          |      "member_targets": null,
          |      "segment_group_targets": [
          |        {
          |          "boolean_operator": "or",
          |          "segments": [
          |            {
          |              "id": 5016,
          |              "action": "include",
          |              "start_minutes": null,
          |              "expire_minutes": null,
          |              "other_less": null,
          |              "other_greater": null,
          |              "other_equals": null,
          |              "code": null,
          |              "name": "test.cat",
          |              "deleted": false,
          |              "other_in_list": null
          |            },
          |            {
          |              "id": 5017,
          |              "action": "include",
          |              "start_minutes": null,
          |              "expire_minutes": null,
          |              "other_less": null,
          |              "other_greater": null,
          |              "other_equals": null,
          |              "code": null,
          |              "name": "test.cm1",
          |              "deleted": false,
          |              "other_in_list": null
          |            }
          |          ]
          |        }
          |      ],
          |      "carrier_targets": null,
          |      "supply_type_targets": null,
          |      "device_type_targets": null,
          |      "screen_size_targets": null,
          |      "device_model_targets": null,
          |      "querystring_targets": null,
          |      "gender_targets": null,
          |      "intended_audience_targets": null,
          |      "inventory_group_targets": null,
          |      "inventory_network_resold_targets": null,
          |      "ip_targets": null,
          |      "operating_system_targets": null,
          |      "operating_system_family_targets": null,
          |      "position_targets": null,
          |      "site_targets": null,
          |      "venue_targets": null,
          |      "operating_system_extended_targets": null,
          |      "mobile_app_instance_targets": null,
          |      "mobile_app_instance_list_targets": null,
          |      "content_category_targets": null,
          |      "deal_targets": null,
          |      "placement_targets": null,
          |      "platform_content_category_targets": null,
          |      "platform_placement_targets": null,
          |      "platform_publisher_targets": null,
          |      "publisher_targets": null,
          |      "segment_targets": null,
          |      "exelate_targets": null,
          |      "ip_range_list_targets": null
          |    },
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 1,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 1,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": false,
          |      "warnings": [],
          |      "time": 279.35695648193,
          |      "start_microtime": 1415608754.4247,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "profile"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseSegmentNames(profileResponseJson) === List("test.cat", "test.cm1"))
    }

    "when segment_target_groups element is null from profile service json response" in {
      val profileResponseJson =
        """{
          |  "response": {
          |    "status": "OK",
          |    "count": 1,
          |    "start_element": 0,
          |    "num_elements": 100,
          |    "profile": {
          |      "id": 302086,
          |      "code": null,
          |      "description": null,
          |      "country_action": "include",
          |      "region_action": "exclude",
          |      "city_action": "exclude",
          |      "browser_action": "exclude",
          |      "use_inventory_attribute_targets": false,
          |      "last_modified": "2013-12-12 22:22:13",
          |      "daypart_timezone": null,
          |      "dma_action": "exclude",
          |      "domain_action": "exclude",
          |      "domain_list_action": "include",
          |      "inventory_action": "exclude",
          |      "language_action": "exclude",
          |      "segment_boolean_operator": "or",
          |      "min_session_imps": null,
          |      "session_freq_type": "platform",
          |      "carrier_action": "exclude",
          |      "supply_type_action": "exclude",
          |      "device_type_action": "exclude",
          |      "screen_size_action": "exclude",
          |      "device_model_action": "exclude",
          |      "location_target_radius": null,
          |      "location_target_latitude": null,
          |      "location_target_longitude": null,
          |      "querystring_action": "exclude",
          |      "querystring_boolean_operator": "and",
          |      "is_expired": false,
          |      "non_audited_url_action": "include",
          |      "advertiser_id": 90781,
          |      "publisher_id": null,
          |      "max_session_imps": null,
          |      "max_day_imps": 3,
          |      "max_lifetime_imps": null,
          |      "max_page_imps": null,
          |      "min_minutes_per_imp": null,
          |      "venue_action": "exclude",
          |      "operating_system_action": "exclude",
          |      "require_cookie_for_freq_cap": true,
          |      "trust": "seller",
          |      "allow_unaudited": false,
          |      "is_template": false,
          |      "created_on": "2013-12-12 22:22:13",
          |      "operating_system_family_action": "exclude",
          |      "use_operating_system_extended_targeting": false,
          |      "mobile_app_instance_action_include": false,
          |      "mobile_app_instance_list_action_include": false,
          |      "user_group_targets": null,
          |      "country_targets": [
          |        {
          |          "country": "NL",
          |          "name": "Netherlands"
          |        }
          |      ],
          |      "region_targets": null,
          |      "city_targets": null,
          |      "inv_class_targets": null,
          |      "inventory_source_targets": null,
          |      "inventory_attribute_targets": null,
          |      "age_targets": null,
          |      "daypart_targets": null,
          |      "browser_targets": null,
          |      "browser_family_targets": null,
          |      "dma_targets": null,
          |      "domain_targets": null,
          |      "domain_list_targets": [
          |        {
          |          "id": 1351,
          |          "name": "CPL",
          |          "description": "Certified publishers list",
          |          "type": "white",
          |          "deleted": false
          |        }
          |      ],
          |      "language_targets": null,
          |      "size_targets": null,
          |      "zip_targets": null,
          |      "member_targets": null,
          |      "segment_group_targets": null,
          |      "carrier_targets": null,
          |      "supply_type_targets": null,
          |      "device_type_targets": null,
          |      "screen_size_targets": null,
          |      "device_model_targets": null,
          |      "querystring_targets": null,
          |      "gender_targets": null,
          |      "intended_audience_targets": null,
          |      "inventory_group_targets": null,
          |      "inventory_network_resold_targets": null,
          |      "ip_targets": null,
          |      "operating_system_targets": null,
          |      "operating_system_family_targets": null,
          |      "position_targets": null,
          |      "site_targets": null,
          |      "venue_targets": null,
          |      "operating_system_extended_targets": null,
          |      "mobile_app_instance_targets": null,
          |      "mobile_app_instance_list_targets": null,
          |      "content_category_targets": null,
          |      "deal_targets": null,
          |      "placement_targets": null,
          |      "platform_content_category_targets": null,
          |      "platform_placement_targets": null,
          |      "platform_publisher_targets": null,
          |      "publisher_targets": null,
          |      "segment_targets": null,
          |      "exelate_targets": null,
          |      "ip_range_list_targets": null
          |    },
          |    "dbg_info": {
          |      "instance": "01.hbapi.client03.lax1",
          |      "slave_hit": false,
          |      "db": "master",
          |      "user::reads": 1,
          |      "user::read_limit": 100,
          |      "user::read_limit_seconds": 60,
          |      "user::writes": 0,
          |      "user::write_limit": 60,
          |      "user::write_limit_seconds": 60,
          |      "reads": 1,
          |      "read_limit": 100,
          |      "read_limit_seconds": 60,
          |      "writes": 0,
          |      "write_limit": 60,
          |      "write_limit_seconds": 60,
          |      "awesomesauce_cache_used": false,
          |      "count_cache_used": false,
          |      "warnings": [],
          |      "time": 279.35695648193,
          |      "start_microtime": 1415608754.4247,
          |      "version": "1.14.221",
          |      "slave_miss": "no_slave_info",
          |      "output_term": "profile"
          |    }
          |  }
          |}""".stripMargin

      assert(JsonUtils.parseSegmentNames(profileResponseJson) === List())
    }
  }

}
